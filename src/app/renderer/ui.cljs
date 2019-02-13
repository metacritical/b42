(ns app.renderer.ui)

(defn webview []
  [:webview {:id "webview"
             :src "https://google.com"
             :useragent "B42 Browser/0.0.1 Chrome/71.0.3578.98 Safari/537.36"
             :style {:display :inline-flex
                     :position "fixed"
                     :top 0 :left 0 :right 0
                     :width "auto"
                     :height "98%"}}])

(defn minibuffer []
  [:div {:id "minibuff-container"
         :style {:background-color "#000"
                 :color "#fff"
                 :width "auto" :height "20px"
                 :position "fixed" :bottom 0
                 :left 0 :right 0}}
   [:input {:id "minibuff" :type "text"
            :style {:background-color "#000"
                    :color "#fff"
                    :outline "none"
                    :border "none"
                    :width "100%"}}]])

(defn app-view []
  [:div {:style {:width "auto"}} [webview][minibuffer]])
    
