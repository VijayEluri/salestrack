{-# LANGUAGE EmptyDataDecls #-}
{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE RebindableSyntax #-}
module Balance where

import FFI
import JQuery hiding (filter, not)
import Fay.Text hiding (map, append)
import Data.Var
import Prelude
import Menu

data Balance = Balance { good:: Good
                       , number:: Int
                       } deriving Eq

main :: Fay ()
main = ready initBalance

initBalance :: Fay ()
initBalance = do
    balanceVar <- newVar []
    _ <- subscribeChange balanceVar renderBalance
    activeSalesVar <- newVar defaultSales
    _ <- subscribeChangeAndRead activeSalesVar $ renderSales (updateActiveSales activeSalesVar)
    _ <- subscribeChangeAndRead activeSalesVar $ loadBalance balanceVar
    return ()

renderBalance :: [Balance] -> Fay ()
renderBalance balance = do
    select "#balance > tbody > tr:not(:has(th))" >>= remove
    element <- select "#balance"
    append renderAll element
    return ()
    where renderAll = foldText $ map render balance
          render b = "<tr><td>" <> (name . good $ b) <> "</td><td>" <> (pack . show $ number b) <> "</td></tr>"

loadBalance :: Var [Balance] -> Sales -> Fay ()
loadBalance balanceVar activeSales  = ajax url (set balanceVar) onFail
    where url = "/journalBalance?me=" <> salesId activeSales
