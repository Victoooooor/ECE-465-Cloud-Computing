import React, { Component } from "react";
import UploadService from "../services/upload-files.service";

class fileInfo {
    constructor(name, type, modification_date) {
        this.name = name;
        this.type = type;
        this.modification_date = modification_date;
    }
}

export default class UploadFiles extends Component {

    state = {
        selectedFile: undefined,
        progress: 0,
        message: "",

        fileInfos: new fileInfo("", "", ""),
    };

    // On file select (from the pop up)
    onFileChange = event => {

        // Update the state
        this.setState({ selectedFile: event.target.files[0] });

    };

    onFileUpload = () => {
        UploadService.upload(this.state.selectedFile, (event) => {
                     this.setState({
                         progress: Math.round((100 * event.loaded) / event.total),
                     });
                 })
            .then(r => {
                console.log(r.toString());
            })
            .then(() => {
                console.log(this.state.selectedFile);
            })
            .then(() => {
                if(this.state.progress === 100 && this.state.selectedFile){
                    this.setState({
                        message: "Upload Success!"
                    })
                    this.setState({
                        fileInfos: new fileInfo(this.state.selectedFile.name, this.state.selectedFile.type, this.state.selectedFile.lastModifiedDate.toDateString())
                    })
                }
                else{
                    this.setState({
                        message: "Upload Failed!"
                    })
                }
            })

    };

    fileData = () => {
        if(this.state.progress === 100 && this.state.selectedFile){
            return(
                <div className="alert alert-light" role="alert">

                    {this.state.message}
                    <p>File name: {this.state.fileInfos.name}</p>
                    <p>File type: {this.state.fileInfos.type}</p>
                    <p>Last Modified: {this.state.fileInfos.modification_date}</p>

                </div>
            );
        } else {
            return (
                <div className="alert alert-light" role="alert">

                    <br />
                    <h4>Choose before Pressing the Upload button</h4>

                </div>
            )
        }
    }

    render() {
        const {
            selectedFile,
            progress,
        } = this.state;

        return (
            <div>
                {selectedFile && (
                    <div className="progress">
                        <div
                            className="progress-bar progress-bar-info progress-bar-striped"
                            role="progressbar"
                            aria-valuenow={progress}
                            aria-valuemin="0"
                            aria-valuemax="100"
                            style={{ width: progress + "%" }}
                        >
                            {progress}%
                        </div>
                    </div>
                )}

                <label className="btn btn-default">
                    <input type="file" onChange={this.onFileChange} />
                </label>

                <button className="btn btn-success"
                        disabled={!selectedFile}
                        onClick={this.onFileUpload}
                >
                    Upload
                </button>

                <div className="alert alert-light" role="alert">

                    {this.fileData()}

                </div>

            </div>
        );
    }
}