(ns base64clj.io
  (:require [clojure.java.io :refer [file output-stream input-stream]]))


(defn write-to-file [file-path barr]
  (with-open [out (output-stream (file file-path))]
    (.write out barr 0 (count barr))))


(defn read-into-barray [file-path]
  (let [f (file file-path)
        inps (input-stream f)
        barr (byte-array (.length f))]
    (.read inps barr)
    (.close inps)
    barr))
