precision mediump float; 
      	 				
varying vec2 v_TextureCoordinates;
uniform sampler2D u_TextureUnit_Day;
uniform sampler2D u_TextureUnit_Night;

varying vec4 v_Ambient; //环境光
varying vec4 v_Diffuse; //散射光
varying vec4 v_Specular;//镜面光

void main()                    		
{
    vec4 finalColorDay;
    vec4 finalColorNight;
    finalColorDay = texture2D(u_TextureUnit_Day,v_TextureCoordinates);
    finalColorDay = finalColorDay*(v_Ambient+v_Specular+v_Diffuse);

    finalColorNight=texture2D(u_TextureUnit_Night,v_TextureCoordinates);
    finalColorNight=finalColorNight*vec4(0.5f,0.5f,0.5f,1.0f);

    if(v_Diffuse.x>0.21){
        gl_FragColor = finalColorDay;
    }else if(v_Diffuse.x<0.05){
        gl_FragColor = finalColorNight;
    }else{
        float t = (v_Diffuse.x - 0.05)/0.16;
        gl_FragColor = t*finalColorDay+(1.0-t)*finalColorNight;
    }

}