{-# LANGUAGE EmptyDataDecls #-}
{-# LANGUAGE OverloadedStrings #-}
module Compare where

import FFI
import JQuery hiding (filter, not)
import Fay.Text.Type
import Data.Var

data Good = Good { id :: String
                 , name :: String
                 } deriving (Eq)
data Transition = Transition { lid :: LID
                             , good :: Good
                             , from :: Sales
                             , to :: Sales
                             , me :: Sales
                             , date :: Int
                             , quantity :: Int
                             , status :: Status 
                             } deriving Eq
data Sales = Sales { salesId :: String
                   , salesName :: String
                   } deriving (Eq)
newtype Formula = Formula Text deriving Eq
newtype LID = LID Text deriving (Eq, Show)
data Status = New | ReadyToSync | Synchronized | Error deriving Eq

main :: Fay()
main = do
    putStrLn "Starting..."
    ready initCompare

initCompare :: Fay ()
initCompare = do
    transVar <- newVar []
    _ <- subscribeChange transVar redrawTransitions
    set transVar [trans "1", trans "2", trans "3"]
    return ()
    where trans lid = Transition (LID lid) (Good "2" "GOOD") (Sales "3" "SALES3") (Sales "5" "SALES5") (Sales "3" "SALES3") 0  1 Error

redrawTransitions :: [Transition] -> Fay ()
redrawTransitions ts = do
    element <- select $ fromString "#transitions"
    append (fromString renderedGroups) element
    return ()
    where groups = nub $ map (salesName . me) ts
          renderedGroups = foldText $ map renderGroup groups
          renderGroup g = foldText $ map (renderTransition g gts) gts
                      where gts = groupTransitions g
          groupTransitions g = filter (\t -> (salesName . me) t == g) ts
          foldText = foldl (++) ""

renderTransition :: String -> [Transition] -> Transition -> String
renderTransition g ts t = "<tr>" ++ spannedGroup ++ "<td>" ++ goodName t ++ " # " ++ fromName t ++ " -> " ++ toName t ++ " " ++ show (quantity t) ++ " # " ++ show (date t) ++ "</td></tr>"
    where meName = salesName . me
          goodName = name . good
          fromName = salesName . from
          toName = salesName . to
          spannedGroup = if (lid t == lid (head ts)) then "<td rowspan=3 style='vertical-align: middle'>" ++ g ++ "</td>" else ""
