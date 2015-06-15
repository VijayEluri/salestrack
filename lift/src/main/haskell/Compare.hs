{-# LANGUAGE EmptyDataDecls #-}
{-# LANGUAGE OverloadedStrings #-}
module Compare where

import FFI
import JQuery hiding (filter, not)
import Fay.Text.Type
import Data.Var
import Data.Ord
import Data.Time

data Transition = Transition { from :: String
                             , to :: String
                             , date :: Day
                             , good :: String
                             , quant :: Int
                             } deriving Eq

main :: Fay()
main = do
    putStrLn "Starting..."
    ready initCompare

initCompare :: Fay ()
initCompare = do
    transVar <- newVar []
    _ <- subscribeChange transVar redrawTransitions
    compareJournals $ set transVar
    return ()

tabled :: String -> String
tabled content = "<table border=1>" ++ content ++ "</table>"

redrawTransitions :: [Transition] -> Fay ()
redrawTransitions ts = do
    element <- select $ fromString "#transitions"
    append (fromString $ tabled renderTransitions) element
    return ()
    where renderTransitions = foldText $ map renderTransition (sortBy (flip $ comparing date) ts)
          foldText = foldl (++) ""

renderTransition :: Transition -> String
renderTransition t = "<tr>" ++ "<td><div style='text-align: center'>" ++ good t ++ "<div></div>" ++ from t ++ " -> " ++ show (quant t)  ++ " -> " ++ to t ++ " # " ++ (unpack . showDay $ date t) ++ "</div></td></tr>"

compareJournals :: ([Transition] -> Fay ()) -> Fay ()
compareJournals onSuccess = ajax url onSuccess onFail
    where url = fromString "/compareJournals"

onFail :: JQXHR -> Maybe Text -> Maybe Text -> Fay ()
onFail _ err status = do
    _ <- errorElement >>= setHtml message
    return ()
    where message = fromString "Call for developers"

errorElement :: Fay JQuery
errorElement = select $ fromString "div[class=error]"
