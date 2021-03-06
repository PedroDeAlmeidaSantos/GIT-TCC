import React, { useEffect, useState } from 'react';
import EmailImg from '../img/emailcheck.png';
import check from '../img/check.png';
import ButtonToolbar from '../../node_modules/react-bootstrap/ButtonToolbar';
import $ from 'jquery';
import axios from 'axios';
import {browserHistory} from 'react-router';
import { DOMINIO } from '../global';
import Botao from '../components/Botao';


import '../css/confirmacao.css';
import ModalSucesso from '../components/ModalSucesso';
import {ModalLoadFun, ModalAlertasFun} from '../components/Modais';

function Confirmacao() {

    const [modalSucessoShow, setModalSucessoShow] = useState(false);
    const [initLoad, setInitLoad] = useState(false);
    const [showAlertas, setModalAlertas] = useState(false);
    const [codeAlerta, setCodeAlerta] = useState([]);
    const [codeConfirm] = useState(random(1000, 9999));
    let [txtCodeConfirm, setTxtCodeConfirm] = useState("");
    const [renderizar, setRenderizar] = useState(true);
    const [profissional] = useState(JSON.parse(localStorage.getItem("profissional")));
    const [cliente] = useState(JSON.parse(localStorage.getItem("cliente")));
    const [endereco] = useState(JSON.parse(localStorage.getItem("endereco")));
    const [tipoAlerta, setTipoAlerta] = useState("erroAlt");
    const [tituloAlerta, setTituloAlerta] = useState("erroAlt");

    function random (min, max){
        return Math.trunc(Math.random() * (max + 1 - min) + min);
    }

    function cadastrarEndereco(){
        axios({
            method: 'POST',
            url: `${DOMINIO}enderecos`,
            type: "application/json",
            timeout: 30000,
            data: {
                bairro: endereco.bairro,
                cep: endereco.cep,
                cidade: {
                    idCidade: endereco.cidade.idCidade
                },
                logradouro: endereco.logradouro,
                numero: endereco.numero
            }
        })
        .then((response)=>{
            let retorno = response.data;
            if(profissional === null){
                cadastrarCliente(retorno.idEndereco);
            }else if(cliente === null){
                cadastrarProfissional(retorno.idEndereco);
            }
        })
        .catch((error)=>{
            setInitLoad(false);
            console.error(error);
        })
        .onload = setInitLoad(true);

        function cadastrarProfissional(idEndereco){
            axios({
                method: 'POST',
                url: `${DOMINIO}profissionais`,
                type: "application/json",
                timeout: 30000,
                data: 
                {
                    cnpj: profissional.cnpj,
                    cpf: profissional.cpf,
                    dataNasc: profissional.dataNasc,
                    email: profissional.email,
                    endereco:{
                        idEndereco: idEndereco
                    },
                    nome: profissional.nome,
                    resumoQualificacoes: profissional.resumoQualificacoes,
                    senha: profissional.senha,
                    subcategoria:{
                        idSubcategoria: profissional.subcategoria.idSubcategoria
                    },
                    tipoUsuario:{
                        idTipoUsuario: profissional.tipoUsuario.idTipoUsuario
                    },
                    valorHora: profissional.valorHora
                }
            })
            .then((response)=>{
                localStorage.clear();
                setInitLoad(false);
                setModalSucessoShow(true);
            })
            .catch((error)=>{
                setInitLoad(false);
                console.error(error);
            })
            .onload = setInitLoad(true);
        }

        function cadastrarCliente(idEndereco){
            axios({
                method: 'POST',
                url: `${DOMINIO}clientes`,
                type: "application/json",
                timeout: 30000,
                data: 
                {
                    cpf: cliente.cpf,
                    dataNasc: cliente.dataNasc,
                    email: cliente.email,
                    endereco:{
                        idEndereco: idEndereco
                    },
                    nome: cliente.nome,
                    senha: cliente.senha,
                    tipoUsuario:{
                        idTipoUsuario: cliente.tipoUsuario.idTipoUsuario
                    }
                }
            })
            .then((response)=>{
                localStorage.clear();
                setInitLoad(false);
                setModalSucessoShow(true);
            })
            .catch((error)=>{
                setInitLoad(false);
                console.error(error);
            })
            .onload = setInitLoad(true);
        }
    }


    function confirmarEmail(){
        let alertas = [];
        let codigo = parseInt(txtCodeConfirm);

        if(codigo === codeConfirm){
            cadastrarEndereco();
        }else{
            alertas.push("Codigo Inválido");
            setCodeAlerta(alertas);
            setTipoAlerta("erroAlt");
            setTituloAlerta("ERRO");
            setModalAlertas(true);
        }
    }

    function definirTxtCodeConfirm(event){
        setTxtCodeConfirm(event.target.value);
        console.log(event.target.value);
    }

    function getUsuario(){
        
        let tipoCadastro;
        let usuario;
        let alertas = [];

        if(profissional === null){
            tipoCadastro = "clientes";
            usuario = cliente
        }else if(cliente === null){
            tipoCadastro = "profissionais";
            usuario = profissional;
        }

        axios({
            method: 'POST',
            url: `${DOMINIO}${tipoCadastro}/confirmacao`,
            timeout: 30000,
            headers: {'token': 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkYXZpZCIsImV4cCI6MTU3MzUwNjI0MCwiaWF0IjoxNTczNDg4MjQwfQ.AlgvbK4_NuSEg9nch72ab_LNL6ZIaCnqV87-6n9AAKjqnTUG4mKqt6CBeuHyC6V8HyEKkhP1_mL05q9cqy3OAw'},
            data: {
                nome: usuario.nome,
                destinatario: usuario.email,
                codigoConfirm: codeConfirm
            }
        })
        .then((response)=>{
            console.log(response);
            console.log("enviado");
            $("#btn-confirm").attr("disabled", false);
            $("#input-cod-confirm").attr("disabled", false);
            alertas.push(`Código de confirmação enviado para ${usuario.email}`);
            setCodeAlerta(alertas);
            setTipoAlerta("msgAlt");
            setTituloAlerta("PRONTO!");
            setInitLoad(false);
            setModalAlertas(true);
        })
        .catch((error)=>{
            setInitLoad(false);
            alertas.push(`Não foi possível enviar o código para ${usuario.email}`);
            setCodeAlerta(alertas);
            setTipoAlerta("erroAlt");
            setTituloAlerta("ERRO!");
            setModalAlertas(true);
            
            console.error(error.response);
        })
        .onload =  setInitLoad(true);
    }

    useEffect(()=>{
        if(renderizar){
            $("#input-cod-confirm").attr("disabled", true);
            $("#btn-confirm").attr("disabled", true);
            console.clear();
            console.log(codeConfirm);
            // setModalAlertas(true);
            // setModalSucessoShow(true);
            getUsuario();
            // setModalSucessoShow(true);
            setRenderizar(false);
        }
    });

    return(
        <section className="container-confirm-email flex-center center">
            <div className="caixa-confirmacao-email flex-center">
                <div className="title-confirmacao center flex-center">
                    Confirmação de E-mail
                </div>
                <div className="text-confirmacao flex-center">
                    <p className="txt-confirm">Um código de verificação foi enviado ao e-mail <span className="negrito">{cliente === null ? profissional.email : cliente.email}</span>. Verifique sua caixa de entrada e escreva o código no campo à baixo para finalizar o cadastro.</p>
                </div>
                <div className="img-email flex-center">
                    <figure>
                        <img src={EmailImg}  alt="Ícone E-mail"/>
                    </figure>
                </div>
                <div className="caixa-input-confirm flex-center center">
                    <div className="caixa-txt-cod-confirm">
                        <input required onChange={definirTxtCodeConfirm} id="input-cod-confirm" className="input-cod-confirm"  type="text" pattern="[0-9]*" maxLength="4" name="cod_email"/>
                    </div>
                    <div className="img-check">
                        <ButtonToolbar>
                            <button onClick={() => confirmarEmail()} data-toggle="modal" data-target="#exampleModalCenter" type="submit" name="button" id="btn-confirm">
                                <figure>
                                    <img src={check} alt="Confirmar" title="Confirmar" /> 
                                </figure>
                            </button>
                            <ModalSucesso
                                show={modalSucessoShow}
                                onHide={() => setModalSucessoShow(false)}/>
                                
                            <ModalAlertasFun
                                erros={codeAlerta}
                                show={showAlertas}
                                titulo={tituloAlerta}
                                tipoAlerta={tipoAlerta}
                                onHide={() => setModalAlertas(false)}/>

                            <ModalLoadFun
                                show={initLoad}
                                />

                        </ButtonToolbar>
                    </div>
                </div>
                <div className="links-email center">
                    <Botao
                        clickBotao={() => getUsuario()}
                        classBotao="link-reenviar-email"
                        valueBotao="Reenviar E-mail"
                    />
                    <Botao
                        clickBotao={()=>{browserHistory.push(profissional === null ? "/cliente/cadastro" : "/profissional/cadastro")}} 
                        classBotao="link-alterar-email"
                        valueBotao="Alterar E-mail"
                    />
                </div>
            </div>
        </section>
    );
   }

   export default Confirmacao;