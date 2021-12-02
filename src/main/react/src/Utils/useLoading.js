import { useEffect } from "react";
import { useState } from "react"

const useLoading = (url) => {
    const [data, setData] = useState();
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        let cancel = false;

        const load = async () => {
            try {
                setIsLoading(true);
                var response = await fetch(url);
                !cancel && setData(response);
            }
            catch (e) {
                console.log(e)
            }
            finally {
                !cancel && setIsLoading(false);
            }
        }

        load();

        return () => { cancel = true }
    }, [url]);

    return [data, isLoading];
}