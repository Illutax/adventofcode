(a => { for(i of a) console.log(i)})
(document.getElementsByTagName("pre")[0]
    .innerText
    .split("\n")
    .map(line => line.split(" ")
                        .map(kv => {kv.split(":")[0] = kv.split(":"[1])})))