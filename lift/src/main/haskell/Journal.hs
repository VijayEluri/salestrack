{-# LANGUAGE EmptyDataDecls #-}
module Journal where

import FFI
import JQuery
import Fay.Text.Type
import Data.Var

data Good = Good String deriving (Eq)
data Transition = Transition Int Int String

class Eventable a
instance Eventable Element

main :: Fay()
main = do
    putStrLn "Starting..."
    ready initJournal

initJournal :: Fay ()
initJournal = do
    putStrLn "Ready..."
    goodVar <- newVar []
    subscribeAndRead goodVar redrawGoods
    transVar <- newVar transitions
    subscribeAndRead transVar redrawTransitions
    lastKeyUpVar <- now >>= newVar
    signalKeyUp lastKeyUpVar goodVar
    elem <- select $ fromString  "#filter"
    keydown onkeyDown elem
    keyup (onkeyUp lastKeyUpVar goodVar) elem
    focus elem
    return ()

signalKeyUp :: Var Int -> Var [Good] -> Fay ()
signalKeyUp timeVar goodsVar = do
    timeVal <- get timeVar
    setTimeout 1000 $ onLoadGoods timeVar timeVal goodsVar

onLoadGoods :: Var Int -> Int -> Var [Good] -> Fay ()
onLoadGoods timestamp tsVal goodsVar = do
    stamp <- get timestamp
    when (stamp == tsVal) $ loadGoods goodsVar

loadGoods :: Var [Good] -> Fay()
loadGoods goodsVar = set goodsVar goods

appendGood :: (Maybe Int, Good) -> Fay JQuery
appendGood good  = do
    elem <-  select $ fromString "#goods"
    append (renderGood good) elem

renderGood :: (Maybe Int, Good) -> Text
renderGood (index, Good goodName) = fromString ("<tr><td>" ++ showIndex index ++ "</td><td class='priceItem' number='1' good_id='goodid'>" ++ goodName ++ "</td></tr>")

redrawGoods :: [Good] -> Fay ()
redrawGoods goods = do
    mapM_ appendGood (zip optionalIndex goods)

redrawTransitions :: [Transition] -> Fay ()
redrawTransitions = mapM_ appendTransition

goods :: [Good]
goods = map (\x -> Good $ "good name " ++ show x) [0..20]

optionalIndex :: [Maybe Int]
optionalIndex = map (\x -> Just x) [0..9] ++ repeat Nothing

showIndex :: Maybe Int -> String
showIndex (Just i) = show i
showIndex Nothing = ""

appendTransition :: Transition -> Fay JQuery
appendTransition transition = do
    elem <- select $ fromString "#journal"
    append (renderTransition transition) elem

renderTransition :: Transition -> Text
renderTransition (Transition goodId me goodName) = fromString(  "<tr good_id='" ++ show goodId ++ "' me='" ++ show me ++ "'><td>11.05.09</td><td>" ++ goodName ++ "</td><td><input type='text' class='newTransition'></td><td><a href='#'>Удалить</a></td></tr>" )

transitions :: [Transition]
transitions = [Transition 1 1 "good name 1", Transition 2 2 "good name 2"]

onkeyUp :: Var Int -> Var [Good] -> Event -> Fay ()
onkeyUp lastKeyUpVar goodsVar e = do
    now >>= set lastKeyUpVar
    signalKeyUp lastKeyUpVar goodsVar

onkeyDown :: Event -> Fay ()
onkeyDown e = do
    when (keycode > 47 && keycode < 58) addtrans
    where keycode = eventKeyCode (e)
          addtrans = do
            appendTransition(head transitions)
            preventDefault e 

eventKeyCode :: Event -> Double
eventKeyCode = ffi "%1.keyCode"

setTimeout :: Int -> Fay () -> Fay ()
setTimeout = ffi "window.setTimeout(%2, %1)"

alert :: String -> Fay ()
alert = ffi "alert(%1)"

now :: Fay (Int)
now = ffi "(new Date).getTime()"
