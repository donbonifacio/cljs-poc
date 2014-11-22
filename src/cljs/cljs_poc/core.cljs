(ns cljs-poc.core
  (:require 
    [kioo.om :refer [content set-attr do-> substitute listen]]
    [kioo.core :refer [handle-wrapper]]
    [om.core :as om :include-macros true]
    [om.dom :as dom :include-macros true])
  (:require-macros [kioo.om :refer [defsnippet deftemplate]])
  )

(defonce app-state (atom {:text "Hello Chestnut!"}))


(defsnippet my-nav-item "index.html" [:.nav-item]
  [[caption func]]
  {[:a] (do-> (content caption)
              (listen :onClick #(func caption)))})

(defsnippet my-header "index.html" [:header]
  [{:keys [heading navigation]}]
  {[:h1] (content heading)
   [:ul] (content (map my-nav-item navigation))})


(deftemplate my-page "index.html"
  [data]
  {[:header] (substitute (my-header data))
   [:.content] (content (:content data))})

(defn init [data] (om/component (my-page data)))

(def app-state2 (atom {:heading "main"
                      :content    "Hello World 1"
                      :navigation [["home alert" #(js/alert %)]
                                   ["next" #(js/alert %)]]}))


(defn main []
  (om/root init app-state2 {:target  (.-body js/document)})
  (om/root
    (fn [app owner]
      (reify
        om/IRender
        (render [_]
          (dom/h1 nil (:text app)))))
    app-state
    {:target (. js/document (getElementById "app"))}))
