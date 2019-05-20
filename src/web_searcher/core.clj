(ns web-searcher.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defn find [query]
  (slurp (str "https://www.bing.com/search?q=" query)))

(defn get-first-search-result-url [html]
  (last (re-find #"b_title.+?<a href=\"(.+?)\"" html)))

(defn get-first [query]
  (-> (find query)
      get-first-search-result-url
      slurp))
