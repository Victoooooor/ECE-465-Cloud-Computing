import React, { Component } from "react";
import SearchFileService from "../services/search-files.service";

export default class SearchFiles extends Component {
    state = {
        keyword: "",
        result: [],
    }

    onKeyChange = event => {
        this.setState({
            keyword: event.currentTarget.value,
        })
    }

    onFileSearch = () => {
        const searchResult = [];
        SearchFileService.search(this.state.keyword)
            .then(r => {
                for(let i = 0; i < r.length; i++){
                    searchResult.push({action: r[i]["action"],
                                       filename: r[i]["filename"],
                                       fid: r[i]["fid"],
                                       hash: r[i]["hash"],
                                       ip: r[i]["ip"],
                                       port: r[i]["port"]});
                }
                console.log(searchResult);
            })
            .then(() => {
                this.setState({
                    result: searchResult,
                })
            })

    };

    showResult = () => {
        if(this.state.keyword && this.state.result.length !== 0){
            return(
                <div className="alert alert-light" role="alert">
                    <p>{this.state.result.length} results found. </p>
                    <div className="card">
                        <div className="card-header">List of Files</div>
                        <ul className="list-group list-group-flush">
                            {this.state.result.map((result, index) =>(
                                <li className="list-group-item" key={index}>
                                    {result.filename}
                                </li>
                            ))}
                        </ul>
                    </div>

                </div>
            );
        } else if(!this.state.keyword){
            return(
                <div className="alert alert-light" role="alert">
                    <p>Please enter a word to search. </p>
                </div>
            );
        } else if(this.state.result.length === 0){
            return(
                <div className="alert alert-light" role="alert">
                    <p>0 result found. </p>
                </div>
            );
        }
    }

    render() {
        const{
            keyword
        } = this.state;

        return (
            <div>
                <label>Search word    </label>
                <rb> </rb>
                <input
                    type="code"
                    placeholder="abcedf"
                    value={this.state.keyword}
                    bg=''
                    onChange={this.onKeyChange}
                />

                <button className="btn btn-success"
                        disabled={!keyword}
                        onClick={this.onFileSearch}
                >
                    Search
                </button>

                <div className="alert alert-light" role="alert">

                    {this.showResult()}

                </div>

            </div>
        );
    }
}