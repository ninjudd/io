(ns io.core
  (:use [clojure.java.io :only [default-streams-impl input-stream IOFactory]])
  (:import (java.nio ByteBuffer)
           (java.io SequenceInputStream)
           (clojure.lang SeqEnumeration Seqable)
           (io.core InputStream InputStreamable)))

(defn bytebuffer->inputstream [^ByteBuffer buf opts]
  (InputStream.
   (reify InputStreamable
     (read [this b offset length]
       (if (.hasRemaining buf)
         (let [length (min length (.remaining buf))]
           (.get buf b offset length)
           length)
         -1))
     (skip [this n]
       (let [n (min n (.remaining buf))]
           (.position buf (+ (.position buf) n))
           n))
     (available [this]
       (.remaining buf))
     (close [this]))))

(extend ByteBuffer
  IOFactory
  (assoc default-streams-impl
    :make-input-stream bytebuffer->inputstream))

(defn concat-input-streams [streams opts]
  (SequenceInputStream.
   (SeqEnumeration.
    (map input-stream streams))))

(extend Seqable
  IOFactory
  (assoc default-streams-impl
    :make-input-stream concat-input-streams))


