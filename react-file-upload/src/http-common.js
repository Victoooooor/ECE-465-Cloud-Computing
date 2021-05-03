import axios from "axios";

export default axios.create({
    baseURL: "http://localhost:4567",
    headers: {
        "Content-type": "application/json"
    }
});