(ns app.main.keyboard
  (:require [electron :refer [globalShortcut]]))

(def key-map (atom {}))
(defonce gsh globalShortcut)

(defn bind-key [key-combo action]
  (.register gsh key-combo action))

(defn register-key-bindings [config])
