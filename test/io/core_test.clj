(ns io.core-test
  (:use clojure.test
        [clojure.java.io :only [input-stream]])
  (:require io.core)
  (:import (java.nio ByteBuffer)))

(deftest byte-buffer-input-stream
  (let [stream (input-stream (ByteBuffer/wrap (byte-array (map byte (range 10)))))]
    (is (= 0 (.read stream)))
    (is (= 1 (.read stream)))
    (is (= 8 (.available stream)))
    (let [buf (byte-array 5)]
      (is (= 5 (.read stream buf)))
      (is (= [2 3 4 5 6] (seq buf)))
      (is (= 3 (.read stream buf 1 4)))
      (is (= [2 7 8 9 6] (seq buf)))
      (is (= -1 (.read stream))))))

(deftest concat-input-stream
  (let [stream (input-stream [(ByteBuffer/wrap (byte-array (map byte (range 5))))
                              (byte-array (map byte (range 5 10)))])]
    (is (= 0 (.read stream)))
    (is (= 1 (.read stream)))
    (is (= 3 (.available stream)))
    (let [buf (byte-array 5)]
      (is (= 3 (.read stream buf)))
      (is (= [2 3 4 0 0] (seq buf)))
      (is (= 2 (.read stream buf 3 2)))
      (is (= [2 3 4 5 6] (seq buf)))
      (is (= 3 (.read stream buf 0 5)))
      (is (= [7 8 9 5 6] (seq buf)))
      (is (= -1 (.read stream))))))

