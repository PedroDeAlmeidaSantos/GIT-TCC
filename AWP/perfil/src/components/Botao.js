import React, { Component } from 'react';
import '../css/login.css';


export class Botao extends Component{
   render(){
    return(
        <button 
            className={this.props.classBotao} 
            id={this.props.idBotao} 
            name={this.props.nameBotao}
            type={this.props.typeBotao}
            >
                {this.props.valueBotao}
        </button>
    );
   }

}

export default Botao;