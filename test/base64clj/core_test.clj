(ns base64clj.core-test
  (:require [clojure.test :refer :all]
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
          target "TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCBieSB0aGlz
IHNpbmd1bGFyIHBhc3Npb24gZnJvbSBvdGhlciBhbmltYWxzLCB3aGljaCBpcyBhIGx1c3Qgb2Yg
dGhlIG1pbmQsIHRoYXQgYnkgYSBwZXJzZXZlcmFuY2Ugb2YgZGVsaWdodCBpbiB0aGUgY29udGlu
dWVkIGFuZCBpbmRlZmF0aWdhYmxlIGdlbmVyYXRpb24gb2Yga25vd2xlZGdlLCBleGNlZWRzIHRo
ZSBzaG9ydCB2ZWhlbWVuY2Ugb2YgYW55IGNhcm5hbCBwbGVhc3VyZS4="]
      (is (and (= (encode-string msg) (->> (strip-newlines target)
                                           (apply str)))
               (= (->> (strip-newlines msg)
                       (apply str)) (decode-to-string target)))))))