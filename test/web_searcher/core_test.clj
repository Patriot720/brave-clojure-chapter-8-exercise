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

(declare test-query-response test-regex-matching)
(deftest search-testing
  (doseq [[engine _] web/engine-data]
    ((juxt test-query-response
           test-regex-matching) engine)))

(deftest multiple-search-engines-test
  (let [result (web/get-first "keepo" [:bing :yahoo])]
    (is (= (count result) 2))))

(defn- test-regex-matching [engine]
  (let [file (str "example-search-" (name engine) ".html")]
    (is (clojure.string/includes?
         (web/get-first-search-result-url (slurp file) engine)
         "http"))))

(defn- test-query-response [engine]
  (let [result (web/find "keepo" engine)]
    (is (seq result))
    (is (clojure.string/includes? result "<body"))))
