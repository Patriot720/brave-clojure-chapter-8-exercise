(ns web-searcher.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(def engine-data
  {:bing {:url "https://www.bing.com/search?q="
          :regex #"b_title.+?<a href=\"(.+?)\""}
   :yahoo {:url "https://search.yahoo.com/search?p="
           :regex #"compTitle options-toggle.+?href=\"(.+?)\""}})

(defn find
  ([query engine]
   (slurp (str (:url (engine engine-data)) query)))
  ([query]
   (find query :bing)))

(defn get-first-search-result-url
  ([html engine]
   (println engine)
   (last (re-find (:regex (engine engine-data)) html)))
  ([html]
   (get-first-search-result-url html :bing)))

(defn get-first
  ([query engines]
   (map (fn [engine]
          (-> (find query engine)
              (get-first-search-result-url engine)
              slurp))
        engines))
  ([query]
   (get-first query :bing)))
