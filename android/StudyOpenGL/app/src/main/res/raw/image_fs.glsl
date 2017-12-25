precision mediump float;
uniform sampler2D u_TextureUnit;

uniform int u_ChangeType;
uniform vec3 u_ChangeData;
uniform vec2 u_ImageSize;


varying vec2 v_TextureCoordinates;
varying vec4 v_Position;


void clamp(vec4 color){
    color.r=max(min(color.r,1.0f),0.0f);
    color.g=max(min(color.g,1.0f),0.0f);
    color.b=max(min(color.b,1.0f),0.0f);
    color.a=max(min(color.a,1.0f),0.0f);
}


void main() {
        vec4 color=texture2D(u_TextureUnit,v_TextureCoordinates);

        if(u_ChangeType==1){//灰度图
            float result=dot(color.rgb,u_ChangeData);
            gl_FragColor=vec4(result,result,result,color.a);
        }else if(u_ChangeType ==2 || u_ChangeType==3){//冷暖色调
            vec4 result=color+vec4(u_ChangeData,0.0f);
            clamp(result);
            gl_FragColor=result;
        }else if(u_ChangeType==4){ //浮雕
            //拾取右下点
            vec4 rightDownColor=texture2D(u_TextureUnit,vec2(v_TextureCoordinates.x-1.0f/u_ImageSize.x, v_TextureCoordinates.y-1.0f/u_ImageSize.y));
            //差值
            vec4 diff=color-rightDownColor;
            vec4 result = diff + vec4(u_ChangeData,color.a);
            clamp(result);
            gl_FragColor=result;
        }else if(u_ChangeType==5){ //图像对比度增强
            float k =0.5f;
            vec3 v= color.rgb;
            vec3 v2= vec3(0.0,0.0,0.0);
            gl_FragColor=vec4(mix(v,v2,k),1.0f);
        }else if(u_ChangeType ==6){ //放大镜
            vec2 imagePos=vec2(u_ImageSize.x*v_TextureCoordinates.s,u_ImageSize.y*v_TextureCoordinates.t);
            vec2 scaleCenterST = vec2(u_ChangeData.x+0.5f,u_ChangeData.y+0.5f);
            //图片的缩放中心点
            vec2 scaleCenterPos = vec2(u_ImageSize.x*scaleCenterST.s,u_ImageSize.y*scaleCenterST.t);
            //float dis=distance(vec2(imagePos.x,imagePos.y),vec2(scaleCenterPos.x,scaleCenterPos.y));
            float dis=sqrt(pow(abs(imagePos.x-scaleCenterPos.x),2.0)+pow(abs(imagePos.y-scaleCenterPos.y),2.0));

            if(dis < u_ChangeData.z){
                float s=((imagePos.x-scaleCenterPos.x)/2.0+scaleCenterPos.x)/u_ImageSize.x;
                float t=((imagePos.y-scaleCenterPos.y)/2.0+scaleCenterPos.y)/u_ImageSize.y;
                color=texture2D(u_TextureUnit,vec2(s,t));
            }

            /*if(abs(imagePos.x-scaleCenterPos.x)<u_ChangeData.z && abs(imagePos.y-scaleCenterPos.y)<u_ChangeData.z){
                float s=((imagePos.x-scaleCenterPos.x)/2.0+scaleCenterPos.x)/u_ImageSize.x;
                float t=((imagePos.y-scaleCenterPos.y)/2.0+scaleCenterPos.y)/u_ImageSize.y;
                color=texture2D(u_TextureUnit,vec2 (s,t));
            }*/
            gl_FragColor=color;

        }else if(u_ChangeType==7){ //马赛克
            float mosaicSize=min(u_ImageSize.x,u_ImageSize.y)*0.01f;
            vec2 imagePos=vec2(v_TextureCoordinates.x*u_ImageSize.x,v_TextureCoordinates.y*u_ImageSize.y);
            vec2 mosaic=vec2(floor(imagePos.x/mosaicSize)*mosaicSize/u_ImageSize.x,floor(imagePos.y/mosaicSize)*mosaicSize/u_ImageSize.y);
            gl_FragColor= texture2D(u_TextureUnit,mosaic);
        }else if(u_ChangeType==8){ //扭曲

        }else if(u_ChangeType == 9){ //颠倒
            gl_FragColor = texture2D(u_TextureUnit,vec2(v_TextureCoordinates.s,1.0f-v_TextureCoordinates.t));
        }else if(u_ChangeType==10){

        }else{
            gl_FragColor = color;
        }
}
