# Haskell advent of code

General documentation can be found here: http://web.archive.org/web/20090306064337/http://darcs.haskell.org/yaht/yaht.pdf

## How to install Haskell

### For Windows

* Install chocolatey on your machine
* run the command in an elevated PowerShell: `choco install haskell-dev haskell-stack`
* accept all by pressing `a`
* refresh by entering `refreshenv`

## How to run the code

### Interactively

* Open the folder in VSCode
* Open a new terminal (CTRL + SHIFT + Ö)
* enter `ghc –interactive` or in short `ghci` to start the interpreter
* with the *load-command* you can load a file into the interpreter with the following schema: `:l hello.hs`
* **Note:** every change to the file `hello.hs` need to be loaded again with the load-command

### Compiling and running the executable

* by entering `ghc --make hello.hs -o hello` in the terminal
* but `ghc hello` works fine as well
* and then just `\.hello.exe`
