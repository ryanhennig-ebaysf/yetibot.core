(ns yetibot.core.test.parser
  (:require
    [yetibot.core.parser :refer :all]
    [instaparse.core :as insta]
    [clojure.test :refer :all]))

(deftest single-cmd-test
  (is (= (parser "uptime")
         [:expr [:cmd [:words "uptime"]]]))
  (is (= (parser "echo qux")
         [:expr [:cmd [:words "echo" [:space " "] "qux"]]])
      "Single commands should be parsed"))

(deftest neighboring-sub-exprs
  (is (= (parser "echo $(echo foo)bar")
         [:expr [:cmd [:words "echo" [:space " "] [:expr [:cmd [:words "echo" [:space " "] "foo"]]] "bar"]]])))

(deftest piped-cmd-test
  (is
    (= (parser "echo hello | echo bar")
       [:expr [:cmd [:words "echo" [:space " "] "hello"]] [:cmd [:words "echo" [:space " "] "bar"]]])
    "Piped commands should be parsed"))

(deftest sub-expr-test
  (is
    (=
     (parser "echo `catfact` | echo It is known:")
     [:expr [:cmd [:words "echo" [:space " "] [:expr [:cmd [:words "catfact"]]]]] [:cmd [:words "echo" [:space " "] "It" [:space " "] "is" [:space " "] "known:"]]])
    "Backtick sub-expressions should be parsed")
  (is
    (=
     (parser "echo $(random)")
     [:expr [:cmd [:words "echo" [:space " "] [:expr [:cmd [:words "random"]]]]]])
    "Standard sub-expressions should be parsed"))

(deftest nested-sub-expr-test
  (is
    (=
     (parser "random | buffer | echo `number $(buffer | peek)`")
     [:expr [:cmd [:words "random"]] [:cmd [:words "buffer"]] [:cmd [:words "echo" [:space " "] [:expr [:cmd [:words "number" [:space " "] [:expr [:cmd [:words "buffer"]] [:cmd [:words "peek"]]]]]]]]])
    "Nested sub-expressions should be parsed")
  (is
    (=
     (parser
       "echo foo
        $(echo bar)")
     [:expr [:cmd [:words "echo" [:space " "] "foo\n" [:space " "] [:space " "] [:space " "] [:space " "] [:space " "] [:space " "] [:space " "] [:space " "] [:expr [:cmd [:words "echo" [:space " "] "bar"]]]]]])
    "Expressions with newlines should preserve the newline")
  (is
    (=
     (parser
       "urban random | buffer | echo `meme wizard: what is $(buffer peek | head)?`
        `meme chemistry: a $(buffer peek | head) is $(buffer peek | head 2 | tail)`")
     [:expr [:cmd [:words "urban" [:space " "] "random"]] [:cmd [:words "buffer"]] [:cmd [:words "echo" [:space " "] [:expr [:cmd [:words "meme" [:space " "] "wizard:" [:space " "] "what" [:space " "] "is" [:space " "] [:expr [:cmd [:words "buffer" [:space " "] "peek"]] [:cmd [:words "head"]]] "?"]]] "\n" [:space " "] [:space " "] [:space " "] [:space " "] [:space " "] [:space " "] [:space " "] [:space " "] [:expr [:cmd [:words "meme" [:space " "] "chemistry:" [:space " "] "a" [:space " "] [:expr [:cmd [:words "buffer" [:space " "] "peek"]] [:cmd [:words "head"]]] [:space " "] "is" [:space " "] [:expr [:cmd [:words "buffer" [:space " "] "peek"]] [:cmd [:words "head" [:space " "] "2"]] [:cmd [:words "tail"]]]]]]]]])
    "Complex nested sub-expressions with newlines should be parsed"))


(deftest literal-test
  (is
    (= (parser "alias foo = \"bar\"")
       [:expr [:cmd [:words "alias" [:space " "] "foo" [:space " "] "=" [:space " "] [:literal "\"" "bar" "\""]]]]))
  (is
    (= (parser "meme foo: \"lol")
       [:expr [:cmd [:words "meme" [:space " "] "foo:" [:space " "] "\"" "lol"]]]))
  (is
    (= (parser "foo \"lol | foo\"")
       [:expr [:cmd [:words "foo" [:space " "] [:literal "\"" "lol | foo" "\""]]]]))
  (is
    (= (parser "foo \"lol | foo")
       [:expr [:cmd [:words "foo" [:space " "] "\"" "lol"]] [:cmd [:words "foo"]]])))

(deftest unmatched-parens-test
  (is
    (= (parser " foo)")
       [:expr [:cmd [:words [:space " "] "foo" ")"]]]))
  (is
    (= (parser "foo)")
       [:expr [:cmd [:words "foo" ")"]]]))
  (is
    (= (parser "foo (")
       [:expr [:cmd [:words "foo" [:space " "] "("]]]))
  (is
    (= (parser " foo")
       [:expr [:cmd [:words [:space " "] "foo"]]])))

(deftest special-character-test
  (is (= (parser "foo \"$\"")
         [:expr [:cmd [:words "foo" [:space " "] [:literal "\"" "$" "\""]]]])
      "Parsing a special character works if it's a literal")
  (is (= (parser "foo $")
         [:expr [:cmd [:words "foo" [:space " "] "$"]]])
      "Parsing with a sub-expr special character works as long as it doesn't fit
       the shape of the beginning of a sub-expr, e.g. $(....."))
