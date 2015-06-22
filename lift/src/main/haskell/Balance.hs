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
main = ready initBalance

initBalance :: Fay ()
initBalance = do
    goodsFilterElement <- goodsFilterInput
    filterVar <- newVar ""
    balanceVar <- newVar []
    _ <- subscribeChange balanceVar renderBalance
    activeSalesVar <- newVar defaultSales
    _ <- subscribeChangeAndRead activeSalesVar $ renderSales (updateActiveSales activeSalesVar)
    bpVar <- mergeVars' (\s f -> BalanceParams s f) Nothing activeSalesVar filterVar
    _ <- subscribeChangeAndRead bpVar $ loadBalance (set balanceVar)
    return ()

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
