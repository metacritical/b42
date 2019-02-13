(ns app.renderer.core
  (:require [reagent.core :as r]
            [cljs.pprint :refer [pprint]]
            [app.renderer.bootstrap :as bootstrap]
            [app.renderer.init :as init]
            [app.renderer.ui :as ui]))

(enable-console-print!)

(defn mount-root [] 
  (r/render [ui/app-view] (.getElementById js/document "app")))

(defn start []
  (bootstrap/init)
  (mount-root))

(defn stop [])

(start)
