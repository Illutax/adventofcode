import Text.Printf

test = "Hello world!" :: [Char] -- declaration with typing to an array of chars
f x y = x + y -- simple function
g = \a b -> a * b -- a lambda function

main :: IO()
main = do -- allows to do state manipulation in an isolated monad
    print test
    printf "Testing f 1 2 = %s\n" (show (f 1 2)) -- formats a string
    printf "Testing g 2 3 = %s\n" (show (g 2 3))
    print ( "Enter name to be greeted: " )
    name <- getLine --read from std::in
    print ( "Hello, " ++ name )