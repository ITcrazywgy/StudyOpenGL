precision highp float;
uniform sampler2D u_TextureUnit;

uniform int u_ChangeType;
uniform vec3 u_ChangeData;
uniform vec2 u_ImageSize;


varying vec2 v_TextureCoordinates;
varying vec4 v_Position;


void clampColor(vec4 color){
    color.r=min(1.0f,max(0.0f,color.r));
    color.g=min(1.0f,max(0.0f,color.g));
    color.b=min(1.0f,max(0.0f,color.b));
    color.a=min(1.0f,max(0.0f,color.a));
}

void main() {
    if(u_ChangeType==1){//灰度图
        vec4 color=texture2D(u_TextureUnit,v_TextureCoordinates);
        float result=dot(color.rgb,u_ChangeData);
        gl_FragColor=vec4(result,result,result,color.a);
    }else if(u_ChangeType ==2 || u_ChangeType==3){//冷暖色调
        vec4 color=texture2D(u_TextureUnit,v_TextureCoordinates);
        vec4 result=color+vec4(u_ChangeData,0.0f);
        clampColor(result);
        gl_FragColor=result;
    }else if(u_ChangeType==4){ //浮雕
        vec4 color=texture2D(u_TextureUnit,v_TextureCoordinates);
        float step = 0.005f;
        //拾取右下点
        vec4 rightDownColor=texture2D(u_TextureUnit,vec2(v_TextureCoordinates.x-step, v_TextureCoordinates.y-step));
        //差值
        vec4 diff=color-rightDownColor;
        vec4 result = diff + vec4(u_ChangeData,color.a);
        clampColor(result);
        gl_FragColor = result;
    }else if(u_ChangeType==5){ //图像对比度增强
        vec4 color=texture2D(u_TextureUnit,v_TextureCoordinates);
        float k =0.5f;
        vec3 v= color.rgb;
        vec3 v2= vec3(0.0,0.0,0.0);
        gl_FragColor=vec4(mix(v,v2,k),1.0f);
    }else if(u_ChangeType ==6){ //放大镜
        vec2 scaleCenterST = vec2(u_ChangeData.s,u_ChangeData.t);
        float s=((v_TextureCoordinates.s-scaleCenterST.s)/2.0+scaleCenterST.s);
        float t=((v_TextureCoordinates.t-scaleCenterST.t)/2.0+scaleCenterST.t);
        gl_FragColor=texture2D(u_TextureUnit,vec2(s,t));
    }else if(u_ChangeType==7){ //马赛克
        float mosaicSize=0.01f;
        vec2 mosaic=vec2(floor(v_TextureCoordinates.s/mosaicSize)*mosaicSize,floor(v_TextureCoordinates.t/mosaicSize)*mosaicSize);
        gl_FragColor= texture2D(u_TextureUnit,mosaic);
    }else if(u_ChangeType==8){ //扭曲
        float PI = 3.14f;
        float R = 0.5f;
        vec2 center = vec2(0.5f,0.5f);
        vec2 current = v_TextureCoordinates;
        vec2 delST = current-center;
        float r = min(R,length(delST));
        //离中心点近，偏移角越大
        float beta= atan(delST.t,delST.s)+ PI*(1.0-(r/R)*(r/R));
        float s = (center.x + r*cos(beta));
        float t = (center.y + r*sin(beta));
        gl_FragColor=texture2D(u_TextureUnit,vec2(s,t));
    }else if(u_ChangeType == 9){ //颠倒
        gl_FragColor = texture2D(u_TextureUnit,vec2(v_TextureCoordinates.s,1.0f-v_TextureCoordinates.t));
    }else if(u_ChangeType==10){ //图像腐蚀
         float wStep=1.0f/u_ImageSize.x;
         float hStep=1.0f/u_ImageSize.y;
         vec4 minColor=vec4(1.0f);
         //找到周围点中最暗的点，代替中间点
         int range = 2;
         for(int i=-range;i<=range;i++){
             for(int j=-range;j<=range;j++){
                 float s=v_TextureCoordinates.s + float(i)*wStep;
                 float t=v_TextureCoordinates.t + float(j)*hStep;
                 minColor=min(texture2D(u_TextureUnit,vec2(s,t)),minColor);
             }
         }
         gl_FragColor=minColor;
    }else if(u_ChangeType==11){//图像膨胀
         float wStep=1.0f/u_ImageSize.x;
         float hStep=1.0f/u_ImageSize.y;
         vec4 maxColor=vec4(-1.0f);
         //找到周围点中最亮的点，代替中间点
         int range = 2;
         for(int i=-range;i<=range;i++){
             for(int j=-range;j<=range;j++){
                 float s=max(0.0,v_TextureCoordinates.s + float(i)*wStep);
                 float t=max(0.0,v_TextureCoordinates.t + float(j)*hStep);
                 maxColor=max(texture2D(u_TextureUnit,vec2(s,t)),maxColor);
             }
         }
         gl_FragColor=maxColor;
    }else if(u_ChangeType == 12){ //普通模糊
         float wStep=1.0f/u_ImageSize.x;
         float hStep=1.0f/u_ImageSize.y;
         int range = 4;
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
    }else if(u_ChangeType == 13){ //高斯模糊
         float wStep=1.0f/u_ImageSize.x;
         float hStep=1.0f/u_ImageSize.y;
         vec4 color=vec4(0.0);
         float factor[9];
         factor[0]=1.0;
         factor[1]=1.0;
         factor[2]=1.0;
         factor[3]=1.0;
         factor[4]=1.0;
         factor[5]=1.0;
         factor[6]=1.0;
         factor[7]=1.0;
         factor[8]=1.0;
         int index = 0;
         for(int i =-1;i<=1;i++){
             for(int j =-1;j<=1;j++){
                float x = max(0.0,v_TextureCoordinates.x + float(i)*wStep);
                float y = max(0.0,v_TextureCoordinates.y + float(i)*hStep);
                color+=texture2D(u_TextureUnit,vec2(x,y)*factor[index++]);
             }
         }
         color/=9.0;
         gl_FragColor=color;
    } else{
        gl_FragColor = texture2D(u_TextureUnit,v_TextureCoordinates);
    }
}
