import http from "../http-common";
import {BACKEND_URL} from "./api";

class SearchFilesService {
    async search(keyword) {
        let response = await fetch(`${BACKEND_URL}/search/${keyword}`);
        if(response.ok){
            return response.json();
        }else {
            return null;
        }
    }

}

export default new SearchFilesService();