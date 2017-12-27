precision mediump float;
uniform sampler2D u_TextureUnit;      	 								
varying vec2 v_TextureCoordinates;      	   								
varying vec4 v_Position;

void clamp(vec4 color){
    color.r=max(min(color.r,1.0f),0.0f);
    color.g=max(min(color.g,1.0f),0.0f);
    color.b=max(min(color.b,1.0f),0.0f);
    color.a=max(min(color.a,1.0f),0.0f);
}

void main()                    		
{
     vec4 color=texture2D(u_TextureUnit,v_TextureCoordinates);
     if(v_Position.y==1.34f){ //top  灰度图
        float result=dot(color.rgb,vec3(0.299f, 0.587f, 0.114f));
        gl_FragColor=vec4(result,result,result,color.a);
     }else if(v_Position.y==-1.34f){ //bottom  浮雕
        //拾取右下点
        float step=0.005f;
        vec4 rightDownColor=texture2D(u_TextureUnit,vec2(v_TextureCoordinates.s-step, v_TextureCoordinates.t-step));
        vec4 diff=color-rightDownColor;
        vec4 result = diff + vec4(0.5f,0.5f,0.5f,color.a);
        clamp(result);
        gl_FragColor=result;
     }else if(v_Position.x==-1.0f){ // left
        gl_FragColor = color;
     }else if(v_Position.x==1.0f){ // right
        gl_FragColor = color;
     }else if(v_Position.z==1.0f){ // near
        gl_FragColor = color;
     }else if(v_Position.z==-1.0f){ //far
        gl_FragColor = color;
     }else{
        gl_FragColor = color;
     }
}