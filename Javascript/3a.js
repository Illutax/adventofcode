(a => {for(i of a)
        for(j of a)
            if (i+j == 2020)
                console.log(i, j, i+j, i*j)})
    (document.getElementsByTagName("pre")[0]
        .innerText
        .split("\n")
        .map(line => line.split("")
                            .map(c => c=="." ? 0 : 1)))