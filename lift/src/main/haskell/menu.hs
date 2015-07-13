{-# LANGUAGE EmptyDataDecls #-}
{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE RebindableSyntax #-}
module Menu where

import FFI
import JQuery hiding (filter, not)
import Fay.Text hiding (head, empty, append)
import qualified Fay.Text as T
import Data.Var
import Model
import Prelude

data Sales = Sales { salesId :: Text
                   , salesName :: Text
                   } deriving (Eq)

updateActiveSales :: Var Sales -> Sales -> Event -> Fay ()
updateActiveSales var s e = do
    set var s

renderSales :: (Sales -> Event -> Fay ()) -> [Sales] -> Sales -> Fay ()
renderSales updateActiveSales sales activeSale = do
    element <- select "#sales > tbody > tr"
    empty element
    mapM_ (appendSale activeSale element) sales
    mapM_ onClickSale sales
    where appendSale activeSale e s = append (render activeSale s) e
          render activeSale s = "<td salesId=" <> salesId s <> " " <> clazz activeSale s <> ">" <> salesName s <> "</td>"
          clazz activeSale s = if s == activeSale then "class='active'" else ""
          onClickSale s = do
            element <- select $ "#sales > tbody > tr > td[salesid = " <> salesId s <> "]"
            click (updateActiveSales s) element 

setActivityColors :: Sales -> Fay ()
setActivityColors activeSale = do
    allSalesElement <- select $ "#sales > tbody > tr > td"
    activeSalesElement <- select $ "#sales > tbody > tr > td[salesId = " <> salesId activeSale <> "]"
    removeClass "active" allSalesElement
    addClass "active" activeSalesElement
    return ()

initMenu :: (Var Sales -> Fay ()) -> Fay ()
initMenu action = do
    ajax url (onSuccess action) onFail
    where url = "/sales"

onSuccess :: (Var Sales -> Fay ()) -> [Sales] -> Fay ()
onSuccess action sales = do
    activeSaleVar <- newVar defaultSales
    _ <- subscribeChange activeSaleVar setActivityColors
    renderSales (updateActiveSales activeSaleVar) sales defaultSales
    action activeSaleVar
    where defaultSales = head sales

