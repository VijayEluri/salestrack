{-# LANGUAGE EmptyDataDecls #-}
{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE RebindableSyntax #-}
module Model where

import FFI
import JQuery hiding (filter, not)
import Fay.Text hiding (head, empty, append)
import qualified Fay.Text as T
import Prelude

(<>) = T.append

data Good = Good { id :: Text
                 , name :: Text
                 } deriving (Eq)

tabled :: Text -> Text
tabled content = "<table border=1>" <> content <> "</table>"

foldText :: [Text] -> Text
foldText = foldl (<>) ""

onFail :: JQXHR -> Maybe Text -> Maybe Text -> Fay ()
onFail _ err status = do
                _ <- errorElement >>= setHtml message
                return ()
                where message = "Call for developers"

errorElement :: Fay JQuery
errorElement = select "div[class=error]"

now :: Fay Int
now = ffi "(new Date).getTime()"

setTimeout :: Int -> Fay () -> Fay ()
setTimeout = ffi "window.setTimeout(%2, %1)"
