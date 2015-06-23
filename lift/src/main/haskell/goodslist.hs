{-# LANGUAGE EmptyDataDecls #-}
{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE RebindableSyntax #-}
module GoodsList where

import FFI
import JQuery hiding (filter, not)
import Fay.Text hiding (head, empty, append, map, null)
import Data.Var
import Prelude
import Model

appendGood :: JQuery -> (Maybe Int, Good) -> Fay JQuery
appendGood element good = append (renderGood good) element

goodsContainerElem :: Fay JQuery
goodsContainerElem = select "#goods"

renderGood :: (Maybe Int, Good) -> Text
renderGood (index, Good id goodName) = "<tr><td>" <> showIndex index <> "</td><td class='priceItem' number='1' good_id='goodid'>" <> goodName <> "</td></tr>"
 
redrawGoods :: [Good] -> Fay ()
redrawGoods goods = do
    element <- goodsContainerElem
    _ <- empty element
    _ <- appendGoodHeader
    mapM_ (appendGood element) (zip optionalIndex goods)

appendGoodHeader :: Fay JQuery
appendGoodHeader = goodsContainerElem >>= append header
    where header = "<tr><th>№</th><th>Товар</th></tr>"

showIndex :: Maybe Int -> Text
showIndex (Just i) = pack $ show i
showIndex Nothing = ""

optionalIndex :: [Maybe Int]
optionalIndex = map (\x -> Just x) [0..9] ++ repeat Nothing

goodsFilterInput :: Fay JQuery
goodsFilterInput = select "#filter"

onkeyUp :: Var Int -> Var [Good] -> Event -> Fay ()
onkeyUp lastKeyUpVar goodsVar _ = do
    now >>= set lastKeyUpVar
    signalKeyUp lastKeyUpVar goodsVar

signalKeyUp :: Var Int -> Var [Good] -> Fay ()
signalKeyUp timeVar goodsVar = do
    timeVal <- get timeVar
    setTimeout 1000 $ onLoadGoods timeVar timeVal goodsVar

onLoadGoods :: Var Int -> Int -> Var [Good] -> Fay ()
onLoadGoods timestamp tsVal goodsVar = do
    stamp <- get timestamp
    when (stamp == tsVal) $ loadGoods goodsVar

loadGoods :: Var [Good] -> Fay()
loadGoods goodsVar = do
    substr <- goodsFilterInput >>= getVal
    ajax (url substr) (onGoodsSuccess goodsVar) onFail
    where url substr = "/goods?substr=" <> substr

onGoodsSuccess :: Var [Good] -> [Good] -> Fay ()
onGoodsSuccess goodsVar goods = set goodsVar goods
