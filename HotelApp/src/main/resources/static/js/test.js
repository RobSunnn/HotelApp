window.addEventListener("load", sendMail);

function sendMail(e) {

    // let token = '';
    //
    //     fetch('http://localhost:8080/token')
    //
    //         .then(response => response.json())
    //         .then(result => {
    //             Object.values(result).forEach(r => console.log(r))
    //         })



// fetch('http://localhost:8080/js/info/' + token)
 fetch('http://localhost:8080/js/info')
       .then(response => response.json())
       .then(result => {



    let params = {
        name: 'Exception',
        email: 'robertospasovrs@gmail.com',
        message: 'hello',

    };

          const publicKey = result[0];
          const serviceID = result[1];
          const templateID = result[2];

        emailjs.send(serviceID, templateID, params, publicKey)
        .then(
            (res) => {

                console.log(res)
                alert('@@@@@     Your Message is Sent Successfully! Thank you :-)     @@@@@')
            }
        )
        .catch(err => console.log(err))

       })

     //     ^^^ make this function to take the values from database in
     //     |||    your application through rest controller

}

//fetch('http://localhost:8080/token')
//    .then(response => {
//        if (!response.ok) {
//            throw new Error('Failed to fetch token.');
//        }
//        return response.text();
//    })
//    .then(token => {
//        // Use the fetched token for subsequent requests
//        fetch(`http://localhost:8080/js/info?token=${token}`)
//            .then(response => {
//                if (!response.ok) {
//                    throw new Error('Authorization failed.');
//                }
//                return response.json();
//            })
//            .then(result => {
//                let params = {
//                        name: 'Exception',
//                        email: 'robertospasovrs@gmail.com',
//                        message: 'hello',
//                    };
//
//                          const publicKey = result[0];
//                          const serviceID = result[1];
//                          const templateID = result[2];
//
//                console.log(publicKey + ' ' + serviceID + ' ' + templateID);
//                        emailjs.send(serviceID, templateID, params, publicKey)
//                        .then(
//                            (res) => {
//
//                                console.log(res)
//                                alert('@@@@@     Your Message is Sent Successfully! Thank you :-)     @@@@@')
//                            }
//                        )
//            })
//            .catch(error => {
//                console.error('Error:', error);
//            });
//    })
//    .catch(error => {
//        console.error('Error:', error);
//    });