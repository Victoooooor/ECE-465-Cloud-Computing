import React from "react";
import "./App.css"
import "bootstrap/dist/css/bootstrap.min.css";

import UploadFiles from "./components/upload-files.component";
import SearchFiles from "./components/search-files.component";

function App() {
    return (
        <div className="container" style={{ width: "600px" }}>
            <div style={{ margin: "20px" }}>
                <h3>ECE465 - Cloud Computing</h3>
                <h4>React upload Files</h4>
            </div>

            <UploadFiles />

            <SearchFiles />
        </div>
    );
}

export default App;
