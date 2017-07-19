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
(def touchable-opacity (partial create-element (.-TouchableOpacity ReactNative)))

(def logo-img (js/require "./images/cljs.png"))

(defn alert [title]
  (.alert (.-Alert ReactNative) title))

(defonce app-state (atom {:current-lesson nil
                          :current-page nil}))

(def menu-rows [; Title Subtitle Lession
                ["Alphabet" "алфавит" 1]
                ["Meeting People" "Знакомство" 2]
                ["Family" "семья" 3]
                ["Where do you work?" "Где вы работаете?" 4]
                ["Where do you live?" "Где вы живете?" 5]
                ["Shopping" "покупки" 6]
                ["In the restaurant" "В ресторане" 7]
                ["Transportation" "транспорт" 8]
                ["In the hotel" "В гостинице" 9]
                ["The telephone" "телефон" 10]])

(def page-count [35 9 8 13 8 27 23 18 18 24])

(def ds (-> (ReactNative.ListView.DataSource.
              (clj->js {:rowHasChanged not=}))
            (.cloneWithRows (clj->js menu-rows))))

(defc AppRoot < rum/reactive [state]
  (let [{:keys [current-lesson current-page]} (rum/react state)]
    (listview {:style {:margin 20}
               :dataSource ds
               :renderRow (fn [row-data]
                            (let [title (aget row-data 0)
                                  subtitle (aget row-data 1)
                                  lesson (aget row-data 2)]
                              (touchable-opacity {:onPress (fn []
                                                             (swap! state assoc :current-lesson lesson))}
                                (view {:style {:margin 5}}
                                  (text {:style {:fontSize 30}} title)
                                  (text {:style {:fontSize 20}} subtitle)))))})))

(defonce root-component-factory (support/make-root-component-factory))

(defn mount-app [] (support/mount (AppRoot app-state)))

(defn init []
  (mount-app)
  (.registerComponent app-registry "Russian101" (fn [] root-component-factory)))

