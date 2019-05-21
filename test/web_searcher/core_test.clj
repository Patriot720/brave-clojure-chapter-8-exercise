(ns web-searcher.core-test
  (:require [clojure.test :refer [deftest deftest- testing is]]
            [web-searcher.core :as web]))

(defn future-variable
  []
  (future (Thread/sleep 1000) 1))

(defn future-variable-1
  []
  (future (Thread/sleep 1000) 1))

(def some-promise (promise))
(future
  (Thread/sleep 1000)
  (deliver some-promise 23))

#_(deftest future-promise-delay-testing
    (testing "Future testing"
      (is (= @(future-variable) 1))
      (testing "Promise testing"
        (is (= @some-promise 23)))))

#_(deftest future-promise-delay-testing-2
    (testing "Future testing"
      (is (= @(future-variable-1) 1))))

(deftest search-testing
  (testing "Test getting html from google"
    (let [result (web/find "keepo")]
      (is (seq result))
      (is (clojure.string/includes? result "Winnie")))
    (let [result (web/find "wtf" :yahoo)]
      (is (seq result))
      (is (clojure.string/includes? result "http://www.wtfpod.com/")))))

(def example-search-bing "example-search-bing.html")
(def example-search-yahoo "example-search-yahoo.html")
(def bing-expected-url "https://keep.google.com/")
(def yahoo-expected-url "http://www.wtfpod.com/")
(deftest get-first-search-result-url-test
  (is (= (web/get-first-search-result-url (slurp example-search-bing))
         bing-expected-url))
  (is (= (web/get-first-search-result-url (slurp example-search-yahoo) :yahoo)
         yahoo-expected-url)))

(deftest multiple-search-engines-test
  (let [result (web/get-first "keepo" [:bing :yahoo])]
    (is (= (count result) 2))))
