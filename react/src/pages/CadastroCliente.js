import React, { Component } from 'react';
import '../css/cadastro-pro.css';
import FormularioCliente from '../components/FormularioCliente';


export class CadastroCliente extends Component{

    render(){
        return(
            
            <div className="container-conteudo-cadastro-pro">
                <div className="caixa-title-cadastro center">
                    <h1>Realize seu Cadastro!</h1>
                </div>
                {/* <form className="form-pro" name="form_profissional" method="GET" onSubmit={this.realizarCadastro}> */}
                    <FormularioCliente/>
                {/* </form> */}
            </div>
        );
    }

}

export default CadastroCliente;
