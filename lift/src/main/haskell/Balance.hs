{-# LANGUAGE EmptyDataDecls #-}
{-# LANGUAGE OverloadedStrings #-}
{-# LANGUAGE RebindableSyntax #-}
module Balance where

import FFI
import JQuery hiding (filter, not)
import Fay.Text (Text, fromString)
import Data.Var
import qualified Fay.Text as T
import Prelude hiding ((++))
import Menu

(++) = T.append

main :: Fay ()
main = ready initBalance

initBalance :: Fay ()
initBalance = do
    activeSalesVar <- newVar defaultSale
    _ <- subscribeChangeAndRead activeSalesVar $ renderSales (updateActiveSales activeSalesVar)
    return ()
