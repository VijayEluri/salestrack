{-# LANGUAGE EmptyDataDecls #-}
{-# LANGUAGE OverloadedStrings #-}
module Menu where

import FFI
import JQuery hiding (filter, not)
import Fay.Text.Type
import Data.Var

data Sales = Sales { salesId :: String
                   , salesName :: String
                   } deriving (Eq)

menuSales :: [Sales]
menuSales = [Sales "3" "Гена", Sales "5" "Наташа", Sales "6" "Екатерина", Sales "7" "Кума"]

defaultSales :: Sales
defaultSales = head menuSales

updateActiveSales :: Var Sales -> Sales -> Event -> Fay ()
updateActiveSales var s e = set var s

renderSales :: (Sales -> Event -> Fay ()) -> Sales -> Fay ()
renderSales updateActiveSales activeSale = do
    element <- select $ fromString "#sales > tbody > tr"
    empty element
    mapM_ (appendSale activeSale element) menuSales
    mapM_ onClickSale menuSales
    where appendSale activeSale e s = append (render activeSale s) e
          render activeSale s = pack $ "<td salesId=" ++ salesId s ++ " class=" ++ clazz activeSale s ++ ">" ++ salesName s ++ "</td>"
          clazz activeSale s = if s == activeSale then "active" else ""
          onClickSale s = do
            element <- select $ fromString $ "#sales > tbody > tr > td[salesId = " ++ salesId s ++ "]"
            click (updateActiveSales s) element 

tabled :: Text -> Text
tabled content = "<table border=1>" ++ content ++ "</table>"
