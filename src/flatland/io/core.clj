(ns flatland.io.core
  (:use [clojure.java.io :only [default-streams-impl input-stream IOFactory]])
  (:import (java.nio ByteBuffer)
           (java.io SequenceInputStream)
           (clojure.lang SeqEnumeration Seqable)
           (flatland.io.core InputStream InputStreamable)))

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

(defn bufseq->bytes [bufs]
  (let [size (apply + (map #(.remaining ^ByteBuffer %) bufs))
        dest (byte-array (apply + (map #(.remaining ^ByteBuffer %) bufs)))]
    (reduce (fn [curr-size ^ByteBuffer b]
              (let [size (.remaining b)]
                (.get b dest curr-size size)
                (+ size curr-size)))
            0 bufs)
    dest))

(defn catbytes [& args]
  (let [out-buf (byte-array (loop [len 0, args (seq args)]
                              (if-let [[arg & args] args]
                                (recur (+ len (alength ^bytes arg)) args)
                                len)))]
    (loop [offset 0, args args]
      (if-let [[^bytes array & more] (seq args)]
        (let [size (alength array)]
          (System/arraycopy array 0
                            out-buf offset size)
          (recur (+ size offset) more))
        out-buf))))
