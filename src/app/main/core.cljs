(ns app.main.core
  (:require [electron :as electron]
            [app.main.cli :as cli]
            [app.main.io :as io]
            [app.main.keyboard :as kb]
            [app.main.bootstrap.node :as boot]
            [cljs.tools.reader :refer [read-string]]
            [cljs.js :refer [empty-state eval eval-str js-eval]]))

(enable-console-print!)

(def main-window (atom nil))
(defonce app electron/app)
(defonce argv (subvec (js->clj js/process.argv) 2))
(defonce ipc-main electron/ipcMain)
(def global-config (atom {:ui-config nil :key-bindings nil}))

;;Bootstrappjng compiler for main process.
(def default-env (empty-state))
(defn eval-form [exp]
  (eval-str default-env
            (str exp)
            "[B42]"
            {:eval       js-eval
             :source-map true
             :verbose false
             :load (partial boot/load default-env)}
            identity))

(defn eval-string [exp]
  (eval default-env
        (read-string exp)
        {:eval       js-eval
         :source-map true
         :verbose false
         :load (partial boot/load default-env)}
        identity))

(defn boot-init []
  (boot/init default-env {:path "app/bootstrap"}
             (fn[]
               ;;Load main init scripts at this point.
               (eval-form "(println \"CLJS bootstrapped ...\")"))))
;;Bootstrapping code ends here.


;; fetches key bindings pre loaded in global-config :key-binding atom
(defn get-key-bindings []
  (@global-config :key-bindings))

;;Load config from some ~/.b42/init.cljs
(defn load-global-config
  ([] (load-file "~/.b42/init.cljs"))
  ([config-file] (load-file config-file)))

(defn init-browser [size]
  (cli/start-msg)
  (let [web-pref (merge
                  (js->clj size)
                  {:title "B42" :webPreferences {:nodeIntegration true}})]

    (reset! main-window (new electron/BrowserWindow (clj->js web-pref)))
    (.loadURL @main-window "http://localhost:3742")

    ;;Call to initialize botstrapping compiler.
    (boot-init)

    ;; (load-global-config)
    (kb/register-key-bindings (get-key-bindings))
    (kb/bind-key "Ctrl+A" (fn[] (println "HI")))
    (.on @main-window "closed" #(reset! main-window nil))))

(defn start-default-window []
  (let[screen (.getPrimaryDisplay electron/screen)
       size screen.size]
    (init-browser size)))

(defn start []
  (cond
    (empty? argv) (start-default-window)
    :else (cli/parse-args argv)))

(defn main []
  (.on app "ready" start)
  (.on ipc-main "load-file" (fn[event path] (io/read-file event path))))
