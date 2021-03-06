(defproject yetibot.core "0.1.8-SNAPSHOT"
  :description "Core yetibot utilities, extracted for shared use among yetibot
                and its various plugins"
  :url "https://github.com/devth/yetibot.core"
  :scm {:name "git" :url "https://github.com/devth/yetibot.core.git"}
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :lein-release {:deploy-via :clojars}
  :aot [yetibot.core.init]
  :main yetibot.core.init
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/data.json "0.1.2"]
                 [org.clojure/tools.namespace "0.2.2"]
                 [org.clojure/tools.trace "0.7.6"]
                 [org.clojure/java.classpath "0.2.0"]
                 [org.clojure/core.cache "0.6.3"]
                 [org.clojure/core.memoize "0.5.6"]
                 [org.clojure/core.match "0.2.0-rc5"]
                 [org.clojure/data.xml "0.0.6"]
                 [org.clojure/data.zip "0.1.1"]
                 [org.clojure/java.jdbc "0.2.3"]

                 ; DurationFormatUtils for uptime
                 [org.apache.commons/commons-lang3 "3.1"]

                 ; logging
                 [com.taoensso/timbre "2.6.2"]

                 ; TODO - kill this some day. We're only relying on it for
                 ; cond-let at this point.
                 [org.clojure/clojure-contrib "1.2.0"]

                 ; parsing
                 [instaparse "1.2.2"]
                 ; parser visualization - disable unless needed
                 ; [rhizome "0.1.9"]

                 ; http
                 [clj-http "0.7.7"]
                 [http.async.client "0.5.2"]

                 ; web
                 [compojure "1.1.5"]
                 [hiccup "1.0.2"]
                 [lib-noir "0.3.4" :exclusions [[org.clojure/tools.namespace]]]

                 ; email
                 [com.draines/postal "1.9.0"]
                 [clojure-mail "0.1.4"]

                 ; chat protocols
                 [clj-campfire "2.2.0"]
                 [irclj "0.5.0-alpha2"]

                 ; database
                 [com.datomic/datomic-free "0.8.3814"]
                 [datomico "0.2.0"]

                 ; javascript evaluation
                 [evaljs "0.1.2"]

                 ; ssh
                 [clj-ssh "0.4.0"]

                 ; wordnik dictionary
                 [clj-wordnik "0.1.0-alpha1"]

                 ; json parsing / schema
                 [com.bigml/closchema "0.1.8"]
                 [cheshire "5.0.1"]

                 ; utils
                 [robert/hooke "1.3.0"]
                 [clj-time "0.4.4"]
                 [rate-gate "1.3.1"]
                 [overtone/at-at "1.0.0"]
                 [inflections "0.7.3"]
                 [environ "0.3.0"]
                 ])
