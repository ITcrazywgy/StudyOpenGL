precision mediump float;
uniform sampler2D u_TextureUnit;      	 								
varying vec2 v_TextureCoordinates;      	   								
varying vec4 v_Position;

void clamp(vec4 color){
    color.r=max(min(color.r,1.0),0.0);
    color.g=max(min(color.g,1.0),0.0);
    color.b=max(min(color.b,1.0),0.0);
    color.a=max(min(color.a,1.0),0.0);
}

void main()                    		
{
     vec4 color=texture2D(u_TextureUnit,v_TextureCoordinates);
     if(v_Position.y==1.34){ //top  灰度图
        float result=dot(color.rgb,vec3(0.299, 0.587, 0.114));
        gl_FragColor=vec4(result,result,result,color.a);
     }else if(v_Position.y==-1.34){ //bottom  浮雕
        //拾取右下点
        float step=0.005;
        vec4 rightDownColor=texture2D(u_TextureUnit,vec2(v_TextureCoordinates.s-step, v_TextureCoordinates.t-step));
        vec4 diff=color-rightDownColor;
        vec4 result = diff + vec4(0.5,0.5,0.5,color.a);
        clamp(result);
        gl_FragColor=result;
     }else if(v_Position.x==-1.0){ // left {0.1f, 0.1f, 0.0f}//暖色调
        vec4 color=texture2D(u_TextureUnit,v_TextureCoordinates);
        vec4 result=color+vec4(0.1, 0.1, 0.0,0.0);
        clamp(result);
        gl_FragColor=result;
     }else if(v_Position.x==1.0){ // right {0.0f, 0.0f, 0.1f}//马赛克
        float mosaicSize=0.01;
        vec2 mosaic=vec2(floor(v_TextureCoordinates.s/mosaicSize)*mosaicSize,floor(v_TextureCoordinates.t/mosaicSize)*mosaicSize);
        gl_FragColor= texture2D(u_TextureUnit,mosaic);
     }else if(v_Position.z==1.0){ // near  原图
         gl_FragColor = color;
     }else if(v_Position.z==-1.0){ //far 普通模糊
         float wStep=0.005;
         float hStep=0.005;
         int range = 1;
         int count = 0;
         vec4 color = vec4(0.0);
         for(int i=-range;i<= range;i++){
             for(int j=-range;j<= range;j++){
                 count++;
                 float s = max(0.0,v_TextureCoordinates.s + float(i)*wStep);
                 float t = max(0.0,v_TextureCoordinates.t + float(j)*hStep);
                 color+=texture2D(u_TextureUnit,vec2(s,t));
             }
         }
         gl_FragColor=color/float(count);
     }else{
        gl_FragColor = color;
     }
}