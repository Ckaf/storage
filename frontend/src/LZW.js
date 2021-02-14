export let LZW = {
    compress: function(uncompressed) {
        "use strict";

        let i, l,
            dictionary = {},
            w = '', k, wk,
            result = [],
            dictSize = 256;

        // initial dictionary
        for (i = 0; i < dictSize; i++) {
            dictionary[String.fromCharCode(i)] = i;
        }

        for (i = 0, l = uncompressed.length; i < l; i++) {
            k = uncompressed.charAt(i);
            wk = w + k;
            if (dictionary.hasOwnProperty(wk)) {
                w = wk;
            }
            else {
                result.push(dictionary[w]);
                dictionary[wk] = dictSize++;
                w = k;
            }
        }

        if (w !== '') {
            result.push(dictionary[w]);
        }

        result.dictionarySize = dictSize;
        return result;
    }
};