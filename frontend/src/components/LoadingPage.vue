<template>
    <div id="popup-bg"></div>

    <div id="popup-container">
        <h2>{{ Title }}</h2>
        <p>{{ Text }}</p>
        <p></p>
        <p></p>
        <p>로딩 중</p>
        <!-- <button id="close-btn" @click="closePop">닫기</button> -->
    </div>
</template>

<script>
export default {
    name: 'PopUp',
    props: {
        Title: String,
        Text: String,
    },
    data() {
        return {
            
            popTitle: "",
            popText: "",


        };
    },
    methods: {
        closePop() {
            this.$emit('closeEvent')
        },
    }
}
</script>