(ns russian101.core
  (:require-macros [rum.core :refer [defc]])
  (:require [re-natal.support :as support]
            [rum.core :as rum]))

(set! js/window.React (js/require "react"))
(def ReactNative (js/require "react-native"))

(defn create-element [rn-comp opts & children]
  (apply js/React.createElement rn-comp (clj->js opts) children))

(def app-registry (.-AppRegistry ReactNative))
(def view (partial create-element (.-View ReactNative)))
(def text (partial create-element (.-Text ReactNative)))
(def image (partial create-element (.-Image ReactNative)))
(def touchable-highlight (partial create-element (.-TouchableHighlight ReactNative)))
(def listview (partial create-element (.-ListView ReactNative)))

(def logo-img (js/require "./images/cljs.png"))

(defn alert [title]
  (.alert (.-Alert ReactNative) title))

(defonce app-state (atom {:greeting "Hello Clojure in iOS and Android!"}))

(def menu-rows [["Alphabet" "алфавит"]
                ["Meeting People" "Знакомство"]
                ["Family" "семья"]
                ["Where do you work?" "Где вы работаете?"]
                ["Where do you live?" "Где вы живете?"]
                ["Shopping" "покупки"]
                ["In the restaurant" "В ресторане"]
                ["Transportation" "транспорт"]
                ["In the hotel" "В гостинице"]
                ["The telephone" "телефон"]])

(def ds (-> (ReactNative.ListView.DataSource.
              (clj->js {:rowHasChanged (fn [r1 r2] (not= r1 r2))}))
            (.cloneWithRows (clj->js menu-rows))))

(defc AppRoot < rum/reactive [state]
  (listview {:style {:margin 20}
             :dataSource ds
             :renderRow (fn [row-data]
                          (view {:style {:margin 5}}
                            (text {:style {:fontSize 30}} (aget row-data 0))
                            (text {:style {:fontSize 20}} (aget row-data 1))))}))

(defonce root-component-factory (support/make-root-component-factory))

(defn mount-app [] (support/mount (AppRoot app-state)))

(defn init []
  (mount-app)
  (.registerComponent app-registry "Russian101" (fn [] root-component-factory)))

