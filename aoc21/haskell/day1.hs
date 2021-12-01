-- `cabal update`
-- `cabal install split`
import Text.Printf

split :: Eq a => a -> [a] -> [[a]]
split d [] = []
split d s = x : split d (drop 1 y) where (x,y) = span (/= d) s

test_input = "199\n200\n208\n210\n200\n207\n240\n269\n260\n263"
-- printf test_input

test_input_splited = split '\n' test_input
-- print test_input_splitted