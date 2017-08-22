(ns russian101.core
  (:require-macros [rum.core :refer [defc]])
  (:require [re-natal.support :as support]
            [rum.core :as rum]))

(set! js/window.React (js/require "react"))
(def ReactNative (js/require "react-native"))
(def ReactNavigation (js/require "react-navigation"))

(defn create-element [rn-comp opts & children]
  (apply js/React.createElement rn-comp (clj->js opts) children))

(def app-registry (.-AppRegistry ReactNative))
(def view (partial create-element (.-View ReactNative)))
(def text (partial create-element (.-Text ReactNative)))
(def image (partial create-element (.-Image ReactNative)))
(def touchable-highlight (partial create-element (.-TouchableHighlight ReactNative)))
(def listview (partial create-element (.-ListView ReactNative)))
(def touchable-opacity (partial create-element (.-TouchableOpacity ReactNative)))
(def stack-navigator (.-StackNavigator ReactNavigation))

(def logo-img (js/require "./images/cljs.png"))

(defn alert [title]
  (.alert (.-Alert ReactNative) title))

(defonce app-state (atom {:current-lesson nil
                          :current-page nil}))

(def menu-rows [; Title, Subtitle, Page Count
                ["Alphabet" "алфавит" 35]
                ["Meeting People" "Знакомство" 9]
                ["Family" "семья" 8]
                ["Where do you work?" "Где вы работаете?" 13]
                ["Where do you live?" "Где вы живете?" 8]
                ["Shopping" "покупки" 27]
                ["In the restaurant" "В ресторане" 23]
                ["Transportation" "транспорт" 18]
                ["In the hotel" "В гостинице" 18]
                ["The telephone" "телефон" 24]])

(def home-ds (-> (ReactNative.ListView.DataSource.
                   (clj->js {:rowHasChanged not=}))
                 (.cloneWithRows (clj->js menu-rows))))

(defc home < rum/reactive [state props]
  (listview {:dataSource home-ds
             :renderRow (fn [row-data sid rid]
                          (let [title (aget row-data 0)
                                subtitle (aget row-data 1)]
                            (touchable-opacity {:onPress (fn []
                                                           (swap! state assoc :current-lesson (int rid))
                                                           (-> props .-navigation (.navigate "lesson")))}
                              (view {:style {:margin 5}}
                                (text {:style {:fontSize 30}} title)
                                (text {:style {:fontSize 20}} subtitle)))))}))

(defc lesson < rum/reactive [state props]
  (let [{:keys [current-lesson]} (rum/react state)
        page-count (get-in menu-rows [current-lesson 2])
        pages (doall
                (for [i (range page-count)]
                  (str "./images/lesson" (inc current-lesson) "/" (inc i) ".png")))]
    (listview {:dataSource (-> (ReactNative.ListView.DataSource.
                                 (clj->js {:rowHasChanged not=}))
                               (.cloneWithRows (clj->js pages)))
               :renderRow (fn [row-data sid rid]
                            (view {:style {:margin 5}}
                              (image {:style {:width 100 :height 100}
                                      :source (js/require row-data)
                                      :resizeMode "contain"})))
               :pageSize 3
               :initialListSize page-count
               :contentContainerStyle {:justifyContent "space-around"
                                       :flexDirection "row"
                                       :flexWrap "wrap"
                                       :alignItems "flex-start"}})))

(defc root < rum/reactive [state]
  (-> (clj->js {:home {:screen #(home state %)
                       :navigationOptions {:title "Russian 101"
                                           :headerBackTitle nil}}
                :lesson {:screen #(lesson state %)
                         :navigationOptions (fn []
                                              (let [{:keys [current-lesson]} @state
                                                    lesson-name (get-in menu-rows [current-lesson 0])]
                                                (clj->js {:title lesson-name})))}})
      (stack-navigator (clj->js {:initialRouteName :home}))
      (create-element {})))

(defonce root-component-factory (support/make-root-component-factory))

(defn mount-app [] (support/mount (root app-state)))

(defn init []
  (mount-app)
  (.registerComponent app-registry "Russian101" (fn [] root-component-factory)))

