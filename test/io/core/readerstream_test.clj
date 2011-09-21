(ns io.core.readerstream-test
  (:use clojure.test)
  (:import [io.core ReadableInputStream IReadable]))

(deftest test-it-works
  (let [byte-seq (atom [[]
                        (map byte (range 10))])

        reader (reify IReadable
                 (read [this b offset length]
                   (let [[before] (swap! byte-seq
                                         (fn [[_ after]]
                                           (split-at length after)))]
                     (if-let [remaining (seq before)]
                       (loop [remaining remaining
                              i 0]
                         (if remaining
                           (do (aset b
                                     (+ i offset)
                                     (first remaining))
                               (recur (next remaining)
                                      (inc i)))
                           i))
                       -1)))
                 (skip [this n]
                   (swap! byte-seq
                          (fn [[before after]]
                            [before (drop n after)]))
                   n)
                 (close [this])
                 (available [this]
                   (count (last @byte-seq))))

        stream (ReadableInputStream. reader)]
    (is (= 0 (.read stream)))
    (is (= 1 (.read stream)))
    (is (= 8 (.available stream)))
    (let [buf (byte-array 5)]
      (is (= 5 (.read stream buf)))
      (is (= [2 3 4 5 6] (seq buf)))
      (is (= 3 (.read stream buf 1 5)))
      (is (= [2 7 8 9 6] (seq buf)))
      (is (= -1 (.read stream))))))