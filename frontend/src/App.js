import React, { Component } from 'react';
import {LZW} from './LZW.js';
export default class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {value: undefined};
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.fileInput = React.createRef();
  }

  handleChange(event) {
    this.setState({value: event.target.value});
  }
  handleSubmit(event) {
    event.preventDefault();
    if (typeof(this.fileInput.current.files[0]) == 'undefined'){
      alert("Error submitting form!");
      return
    }
    let data=this.fileInput.current.files[0]
    let name=this.fileInput.current.files[0].name

    let newData;
    let reader = new FileReader();
    reader.onload = function(){
      let text = reader.result;
      text = LZW.compress(text)
      text=(text.join('|'));
      let formData = new FormData();
      formData.append('name',name)
      formData.append('file',text.toString())
      fetch("http://localhost:2020/upload", {
        mode: 'no-cors',
        method: "POST",
        body:formData
      }).then(function (response){
        // console.log(response.body)
      })
    }
    reader.readAsText(data)





  }
  render(){
    return (
        <form onSubmit={this.handleSubmit}>
          <label>
            <br/>File to upload:
            <input type="file" name="file" ref={this.fileInput} onChange={this.handleChange}/>
          </label>
          <br/><input type="submit" value="Send" />
        </form>
    );
  }
}