import React, { Component, Fragment } from 'react';
import '../css/perfil-pro.css';
import '../css/bootstrap.css';
import '../css/padroes.css';


export class AvaliacaoPro extends Component{
   render(){
    return(
 
        <div class="caixa-comentarios">
            <h2 class="title-avaliacao">Principais Avaliações</h2>
            <div class="caixa-comentario-usuario">
                <div class="usuario">
                    <div class="circulo-usuario"></div>
                    <h4 class="nome-usuario">TitleTitle</h4>
                </div>
                <div class="dados-usuario">
                    <h4 class="titulo-comentario">TextText TextText</h4>
                    <div class="caixa-star">
                        <div class="estrelas">
                            <input type="radio" id="cm_star-empty" name="fb" value="" checked/>
                            <label for="cm_star-1"><i class="fa"></i></label>
                            <input type="radio" id="cm_star-1" name="fb" value="1"/>
                            <label for="cm_star-2"><i class="fa"></i></label>
                            <input type="radio" id="cm_star-2" name="fb" value="2"/>
                            <label for="cm_star-3"><i class="fa"></i></label>
                            <input type="radio" id="cm_star-3" name="fb" value="3"/>
                            <label for="cm_star-4"><i class="fa"></i></label>
                            <input type="radio" id="cm_star-4" name="fb" value="4"/>
                            <label for="cm_star-5"><i class="fa"></i></label>
                            <input type="radio" id="cm_star-5" name="fb" value="5" checked/>
                        </div>
                    </div>
                    <p class="texto-comentario">Type something herehjtyutyjyujuyj Type something ghatgh ghathtg hereType something here.</p>
                </div>
            </div>
            <div class="caixa-comentario-usuario">
                <div class="usuario">
                    <div class="circulo-usuario"></div>
                    <h4 class="nome-usuario">TitleTitle</h4>
                </div>
                <div class="dados-usuario">
                    <h4 class="titulo-comentario">TextText TextText</h4>
                    <div class="caixa-star">
                        <div class="estrelas">
                            <input type="radio" id="cm_star-empty" name="fb" value="" checked/>
                            <label for="cm_star-1"><i class="fa"></i></label>
                            <input type="radio" id="cm_star-1" name="fb" value="1"/>
                            <label for="cm_star-2"><i class="fa"></i></label>
                            <input type="radio" id="cm_star-2" name="fb" value="2"/>
                            <label for="cm_star-3"><i class="fa"></i></label>
                            <input type="radio" id="cm_star-3" name="fb" value="3"/>
                            <label for="cm_star-4"><i class="fa"></i></label>
                            <input type="radio" id="cm_star-4" name="fb" value="4"/>
                            <label for="cm_star-5"><i class="fa"></i></label>
                            <input type="radio" id="cm_star-5" name="fb" value="5" checked/>
                        </div>
                    </div>
                    <p class="texto-comentario">Type something herehjtyutyjyujuyj Type something ghatgh ghathtg hereType something here.</p>
                </div>
            </div>
        </div>
    );
   }

}

export default AvaliacaoPro;