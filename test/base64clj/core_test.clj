(ns base64clj.core-test
  (:require [clojure.test :refer :all]
            [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :refer [defspec]]
            [base64clj.encode :refer :all]
            [base64clj.decode :refer :all]))

(deftest simple-test
  (testing "Simple case"
    (let [target "Send reinforcements"
          wanted "U2VuZCByZWluZm9yY2VtZW50cw=="]
      (is (and (= (encode-string target)
                  wanted)
               (= (decode-to-string wanted)
                  target))))))

(deftest newlines-test
  (testing "Large with newlines"
    (let [msg "Man is distinguished, not only by his reason, but by this singular passion from other animals, which is a lust of the mind, that by a perseverance of delight in the continued and indefatigable generation of knowledge, exceeds the short vehemence of any carnal pleasure."
          target "TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCBieSB0aGlzIHNpbmd1bGFyIHBhc3Npb24gZnJvbSBvdGhlciBhbmltYWxzLCB3aGljaCBpcyBhIGx1c3Qgb2YgdGhlIG1pbmQsIHRoYXQgYnkgYSBwZXJzZXZlcmFuY2Ugb2YgZGVsaWdodCBpbiB0aGUgY29udGludWVkIGFuZCBpbmRlZmF0aWdhYmxlIGdlbmVyYXRpb24gb2Yga25vd2xlZGdlLCBleGNlZWRzIHRoZSBzaG9ydCB2ZWhlbWVuY2Ugb2YgYW55IGNhcm5hbCBwbGVhc3VyZS4="]
      (is (and (= (encode-string msg)
                  target)
               (= msg
                  (decode-to-string target)))))))

(def byte-arrays-encoding-decoding
  (prop/for-all [bs (gen/such-that not-empty (gen/vector gen/byte))]
                (let [barr (byte-array bs)
                      encoding (encode barr)
                      decoded (decode encoding)]
                  (= (seq barr) (seq decoded)))))

(defspec byte-arrays-encoding-decoding-test 10000 byte-arrays-encoding-decoding)
