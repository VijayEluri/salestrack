{-# LANGUAGE EmptyDataDecls #-}
{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE RebindableSyntax #-}
module Compare where

import FFI
import JQuery hiding (filter, not)
import Fay.Text (Text, fromString)
import Data.Var
import Data.Ord
import qualified Fay.Text as T
import Prelude hiding ((++))
import Menu

(++) = T.append

data Transition = Transition { from :: Text
                             , to :: Text
                             , date :: Int
                             , renderedDate :: Text
                             , good :: Text
                             , quant :: Int
                             } deriving Eq

main :: Fay()
main = do
    putStrLn $ T.unpack "Starting..."
    ready initCompare

initCompare :: Fay ()
initCompare = do
    activeSaleVar <- newVar defaultSales
    _ <- subscribeChangeAndRead activeSaleVar $ renderSales (updateActiveSales activeSaleVar)
    transVar <- newVar []
    _ <- subscribeChange transVar redrawTransitions
    compareJournals $ set transVar
    return ()

tabled :: Text -> Text
tabled content = "<table border=1>" ++ content ++ "</table>"

redrawTransitions :: [Transition] -> Fay ()
redrawTransitions ts = do
    element <- select "#transitions"
    append (tabled renderTransitions) element
    return ()
    where renderTransitions = foldText $ map renderTransition (sortBy (flip $ comparing date) ts)
          foldText = foldl (++) ""

renderTransition :: Transition -> Text
renderTransition t = "<tr>" ++ "<td><div style='text-align: center'>" ++ (good t) ++ "<div></div>" ++ (from t) ++ " -> " ++ (T.pack . show $ quant t)  ++ " -> " ++ (to t) ++ "</div></td><td>" ++ renderedDate t ++ "</td></tr>"

compareJournals :: ([Transition] -> Fay ()) -> Fay ()
compareJournals onSuccess = ajax url onSuccess onFail
    where url = "/compareJournals"

onFail :: JQXHR -> Maybe Text -> Maybe Text -> Fay ()
onFail _ err status = do
    _ <- errorElement >>= setHtml message
    return ()
    where message = "Call for developers"

errorElement :: Fay JQuery
errorElement = select "div[class=error]"
