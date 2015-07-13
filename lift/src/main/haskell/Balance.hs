{-# LANGUAGE EmptyDataDecls #-}
{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE RebindableSyntax #-}
module Balance where

import FFI
import JQuery hiding (filter, not)
import Fay.Text hiding (map, append)
import Data.Var
import Prelude
import Model
import GoodsList
import Menu

data Balance = Balance { good:: Good
                       , number:: Int
                       } deriving Eq

data BalanceParams = BalanceParams { sale :: Sales
                                   , fltr :: Text
                                   } deriving Eq

main :: Fay ()
main = ready $ initMenu initBalance

initBalance :: Var Sales -> Fay ()
initBalance activeSalesVar = do
    goodsFilterElement <- goodsFilterInput
    filterVar <- newVar ""
    timeStampVar <- now >>= newVar
    keyup (delayedSetFilterVar timeStampVar (onTimeout timeStampVar filterVar goodsFilterElement)) goodsFilterElement
    balanceVar <- newVar []
    _ <- subscribeChange balanceVar renderBalance
    bpVar <- mergeVars' (\s f -> BalanceParams s f) Nothing activeSalesVar filterVar
    _ <- subscribeChangeAndRead bpVar $ loadBalance (set balanceVar)
    return ()

onTimeout :: Var Int -> Var Text -> JQuery -> Int -> Fay ()
onTimeout timeStampVar filterVar filterElement timestamp = do
    ts <- get timeStampVar
    when (ts == timestamp) $ getVal filterElement >>=  set filterVar 

delayedSetFilterVar :: Var Int -> (Int -> Fay ()) -> Event -> Fay ()
delayedSetFilterVar timestampVar onTimeout _ = do
    ts <- now
    set timestampVar ts
    setTimeout 1000 $ onTimeout ts

renderBalance :: [Balance] -> Fay ()
renderBalance balance = do
    select "#balance > tbody > tr:not(:has(th))" >>= remove
    element <- select "#balance"
    append renderAll element
    return ()
    where renderAll = foldText $ map render balance
          render b = "<tr><td>" <> (name . good $ b) <> "</td><td>" <> (pack . show $ number b) <> "</td></tr>"

loadBalance :: ([Balance] -> Fay ()) -> BalanceParams -> Fay ()
loadBalance onSuccess balanceParams = ajax url onSuccess onFail
    where url = "/journalBalance?me=" <> salesId (sale balanceParams) <> "&fltr=" <> fltr balanceParams
