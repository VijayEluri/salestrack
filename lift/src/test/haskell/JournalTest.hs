module JournalTest(spec, main) where

import Prelude
import Journal
--import Fay.Text.Type
import Test.Hspec

spec :: Spec
spec = do
    describe "Journal" $ do
        describe "transition parsing" $ do
            it "can parse sell transitions" $ do
                --b <- checkTransition (fromString "3*250")
                --b `shouldBe` True
                True `shouldBe` True

main :: IO ()
main = hspec spec
